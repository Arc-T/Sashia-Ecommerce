import { FieldGroup } from "@/components/ui/field";
import { useGetAttribute } from "@/hooks/use-attributes";
import {
  PenBoxIcon,
  Edit,
  Trash2,
  Calendar,
  Tag,
  Filter,
  Info,
  Copy,
  Folder,
  FileText,
  Search,
} from "lucide-react";
import { useTranslation } from "react-i18next";
import { useParams, useNavigate } from "react-router";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { useState } from "react";
import { CustomField } from "@/components/custom/elements/field";
import { CustomIsLoadingPage } from "@/pages/is-loading";
import { CustomIsErrorPage } from "@/pages/is-error";
import { CustomNoResourcePage } from "@/pages/no-resource";

export function AttributesShow() {
  const { t } = useTranslation();
  const { id } = useParams();
  const navigate = useNavigate();
  const [isCopied, setIsCopied] = useState(false);

  const { data: attribute, isLoading, error } = useGetAttribute(Number(id));

  // Handle loading state
  if (isLoading) {
    return <CustomIsLoadingPage />;
  }

  // Handle error state
  if (error) {
    return <CustomIsErrorPage />;
  }

  // No ID selected
  if (!id) {
    return (
      <CustomNoResourcePage
        title="select_attribute_first"
        header="select_attribute"
        description="select_attribute_description"
        resourcePath="/attributes"
        resourceLabel={t("Browse Attributes")}
      />
    );
  }

  const handleCopyId = () => {
    navigator.clipboard.writeText(id);
    setIsCopied(true);
    setTimeout(() => setIsCopied(false), 2000);
  };

  const handleEdit = () => {
    navigate(`/attributes/${id}/edit`);
  };

  const handleDelete = () => {
    // Add your delete confirmation logic here
    if (confirm(t("Are you sure you want to delete this attribute?"))) {
      // Handle delete
    }
  };

  return (
    <>
      {/* Header Section */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
        <div className="flex items-center gap-4">
          {/* Back Button */}
          {/*<Button
            variant="ghost"
            size="icon"
            onClick={() => navigate(-1)}
            className="h-10 w-10 shrink-0"
          >
            <ArrowLeft className="h-5 w-5" />
          </Button>*/}

          {/* Icon */}
          <div className="rounded-xl bg-primary/10 p-3">
            <PenBoxIcon className="h-6 w-6 text-primary" />
          </div>

          <div>
            <h2 className="text-2xl font-bold tracking-tight">
              {attribute?.name || t("attribute_details")}
            </h2>
            <p className="flex items-center gap-2 text-sm text-muted-foreground">
              <span>{t("view_attribute_information")}</span>
              <span className="h-1 w-1 rounded-full bg-muted-foreground/30" />
              <span className="font-mono text-xs">{t("id") + `:${id}`}</span>
              <Button
                variant="ghost"
                size="sm"
                className="h-6 px-2 text-xs"
                onClick={handleCopyId}
              >
                {isCopied ? t("Copied!") : <Copy className="h-3 w-3" />}
              </Button>
            </p>
          </div>
        </div>

        {/* Action Buttons */}
        <div className="flex gap-2 self-start sm:self-center">
          <Button
            variant="outline"
            size="sm"
            onClick={handleEdit}
            className="gap-2"
          >
            <Edit className="h-4 w-4" />
          </Button>
          <Button
            variant="outline"
            size="sm"
            onClick={handleDelete}
            className="gap-2"
          >
            <Trash2 className="h-4 w-4" />
          </Button>
        </div>
      </div>

      {/* Details Card */}
      <div className="mt-6 rounded-2xl border border-border bg-card shadow-sm">
        {/* Status Bar */}
        <div className="flex flex-wrap items-center justify-between border-b border-border px-4 py-3 sm:px-6">
          <div className="flex flex-wrap items-center gap-3">
            <Badge variant="outline" className="gap-1">
              <Tag className="h-3 w-3" />
              {attribute?.category?.name || t("Uncategorized")}
            </Badge>
            <Badge
              variant={attribute?.isFilterable ? "default" : "secondary"}
              className="gap-1"
            >
              <Filter className="h-3 w-3" />
              {attribute?.isFilterable ? t("Filterable") : t("Not Filterable")}
            </Badge>
          </div>
          <div className="flex items-center gap-2 text-xs text-muted-foreground">
            <Calendar className="h-3 w-3" />
            <span>
              {t("Created")}: {new Date().toLocaleDateString()}
            </span>
          </div>
        </div>

        {/* Fields Grid */}
        <div className="p-4 sm:p-6">
          <FieldGroup className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-3">
            <CustomField
              label="name"
              value={attribute?.name}
              icon={<Tag className="h-4 w-4" />}
            />

            <CustomField
              label="category"
              value={
                attribute?.category?.name || (
                  <span className="text-muted-foreground/60">
                    {t("No category")}
                  </span>
                )
              }
              icon={<Folder className="h-4 w-4" />}
            />

            <CustomField
              label={"filterable"}
              value={
                <Badge
                  variant={attribute?.isFilterable ? "default" : "secondary"}
                >
                  {attribute?.isFilterable ? t("Enabled") : t("Disabled")}
                </Badge>
              }
              icon={<Filter className="h-4 w-4" />}
            />

            <CustomField
              label="status"
              value={
                <Badge variant="link" className="gap-1">
                  <span className="h-1.5 w-1.5 rounded-full bg-green-500" />
                  {t("Active")}
                </Badge>
              }
              icon={<Info className="h-4 w-4" />}
            />

            <CustomField
              label="created_at"
              value={attribute?.createdAt}
              icon={<Calendar className="h-4 w-4" />}
            />

            <CustomField
              label="updated_at"
              value={attribute?.updatedAt}
              icon={<Calendar className="h-4 w-4" />}
            />

            {attribute?.values && attribute.values.length > 0 && (
              <div className="col-span-3">
                <CustomField
                  label={"values"}
                  value={attribute.values.map((value) => {
                    return (
                      <Badge variant={"default"} className="mx-1">
                        {value.value}
                      </Badge>
                    );
                  })}
                  icon={<Search className="h-4 w-4" />}
                />
              </div>
            )}

            <div className="col-span-3">
              <CustomField
                label="description"
                value={attribute?.description}
                icon={<FileText className="h-4 w-4" />}
              />
            </div>
          </FieldGroup>
        </div>

        {/* Footer Actions */}
        <div className="flex flex-wrap items-center justify-between gap-4 border-t border-border px-4 py-3 sm:px-6">
          <div className="flex items-center gap-2 text-xs text-muted-foreground">
            <span>{t("need_help")}</span>
            <Button variant="link" size="sm" className="h-auto px-1 text-xs">
              {t("view_docs")}
            </Button>
          </div>
        </div>
      </div>
    </>
  );
}
