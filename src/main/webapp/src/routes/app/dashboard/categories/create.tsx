import { RHFSubmitButton } from "@/components/custom/elements/form/button";
import { RHFInput } from "@/components/custom/elements/form/input";
import { RHFSelect } from "@/components/custom/elements/form/select";
import { RHFSwitch } from "@/components/custom/elements/form/switch";
import { FieldGroup } from "@/components/ui/field";
import { useCreateAttribute } from "@/hooks/use-attributes";
import { useGetCategories } from "@/hooks/use-categories";
import { mapToSelectOptions } from "@/lib/utils";
import {
  useCreateAttributeForm,
  type CreateAttributeFormValues,
} from "@/types/attribute";
import { PenBoxIcon } from "lucide-react";

export function CategoriesCreate() {
  const { isPending: isSubmitting, mutate } = useCreateAttribute();
  const { data: categories, isPending, isError } = useGetCategories();
  const form = useCreateAttributeForm();

  const onSubmit = (values: CreateAttributeFormValues) => {
    mutate(values);
  };

  return (
    <>
      <div className="flex items-center gap-4 pb-3">
        <div className="p-3 bg-primary/10 rounded-xl">
          <PenBoxIcon className="w-6 h-6 text-primary" aria-hidden />
        </div>
        <div>
          <h2 className="text-xl font-semibold text-foreground">
            ایجاد ویژگی جدید
          </h2>
          <p className="text-sm text-muted-foreground">
            اطلاعات ویژگی را وارد کنید تا به محصولات اضافه شود.
          </p>
        </div>
      </div>

      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="w-full flex flex-col gap-4 rounded-2xl border border-border bg-card shadow-sm p-4 sm:p-6"
        aria-busy={isSubmitting}
      >
        <FieldGroup className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* <div className="grid grid-cols-1 lg:grid-cols-2 gap-6"> */}
          <RHFInput
            name="name"
            control={form.control}
            label="name"
            placeholder="attribute_name_placeholder"
          />

          <RHFSelect
            name="categoryId"
            isPending={isPending}
            control={form.control}
            isError={isError}
            label={"category"}
            description={"select_category"}
            options={mapToSelectOptions(categories?.content)}
          />

          <RHFSwitch
            name="isFilterable"
            control={form.control}
            label="multi_factor_authentication"
            description="enable_mfa_description"
          />

          <RHFSubmitButton isSubmitting={isSubmitting} />
        </FieldGroup>
      </form>
    </>
  );
}
